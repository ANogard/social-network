package ru.example.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.exception.ExceptionText;
import ru.example.socnetwork.exception.InvalidRequestException;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.Tag;
import ru.example.socnetwork.model.rsdto.postdto.NewPostDto;
import ru.example.socnetwork.repository.Post2TagRepository;
import ru.example.socnetwork.repository.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@DebugLogs
public class TagService {

  private final TagRepository tagRepository;
  private final Post2TagRepository post2TagRepository;

  public List<String> getPostTags(int postId) {

    return tagRepository
            .getPostTags(postId)
            .stream()
            .map(Tag::getTagName)
            .collect(Collectors.toList());
  }

  public List<String> getAllTags() {

    return tagRepository
            .getAllTags()
            .stream()
            .map(Tag::getTagName)
            .collect(Collectors.toList());
  }

  public void addTag(String tag) throws InvalidRequestException {

    if (tag.length() > Constants.MAX_TAG_LENGTH) {
      throw new InvalidRequestException(
              Constants.MAX_TAG_LENGTH + ExceptionText.TAG_MAX_LENGTH.getMessage() + tag.length());
    } else {
      tagRepository.addTag(tag);
    }
  }

  public void addTagsFromNewPost(int postId, NewPostDto newPostDto) throws InvalidRequestException {

    List<Tag> tagList = tagRepository.getAllTags();
    List<String> postTags = newPostDto.getTags();
    for (String tag : postTags) {
      if (getTagId(tagList, tag) == -1) {
        this.addTag(tag);
      }
    }
    List<Tag> newTagsList = tagRepository.getAllTags();
    postTags.forEach(tag -> post2TagRepository.addTag2Post(postId, getTagId(newTagsList, tag)));
  }

  public void editOldTags(int postId, NewPostDto newPostDto) throws InvalidRequestException {

    List<Tag> allTags = tagRepository.getAllTags();
    post2TagRepository.deletePostTags(postId);
    HashSet<String> postTagsSet = new HashSet<>(newPostDto.getTags());

    for (String tag : postTagsSet) {
      if (getTagId(allTags, tag) == -1) {
        this.addTag(tag);
      }
    }

    List<Tag> newTagsList = tagRepository.getAllTags();
    postTagsSet.forEach(tag -> post2TagRepository.addTag2Post(postId, getTagId(newTagsList, tag)));
  }

  private int getTagId(List<Tag> tags, String tag) {

    return tags.stream().filter(t -> t.getTagName().equals(tag)).findFirst().orElse(new Tag()).getId();
  }

  public void deletePostTags(int postId) {
    post2TagRepository.deletePostTags(postId);
  }
}
