package ru.example.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.exception.ExceptionText;
import ru.example.socnetwork.exception.InvalidRequestException;
import ru.example.socnetwork.model.entity.CommentLike;
import ru.example.socnetwork.model.rsdto.postdto.LikedDto;
import ru.example.socnetwork.model.rsdto.postdto.LikesDto;
import ru.example.socnetwork.repository.CommentLikeRepository;
import ru.example.socnetwork.repository.PostLikeRepository;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.PostLike;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@DebugLogs
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostService postService;
    private final SecurityPerson securityPerson = new SecurityPerson();


    public LikesDto getLikes(Integer itemId, String type) throws InvalidRequestException {
        LikesDto likesDto = new LikesDto();
        if (type.equals(Constants.POST)) {
            List<PostLike> postLikeList = postLikeRepository.getPostLikes(itemId);
            likesDto.setLikes(postLikeList.size());
            likesDto.setUsers(postLikeList.stream().map(PostLike::getPersonId).collect(Collectors.toList()));
            return likesDto;
        } else if (type.equals(Constants.COMMENT)) {
            List<CommentLike> likeList = commentLikeRepository.getLikes(itemId);
            likesDto.setLikes(likeList.size());
            likesDto.setUsers(likeList.stream().map(CommentLike::getPersonId).collect(Collectors.toList()));
            return likesDto;
        }
        throw new InvalidRequestException(ExceptionText.LIKE_WRONG_TYPE.getMessage());
    }

    public LikesDto putAndGetAllLikes(Integer itemId, String type) throws InvalidRequestException {
        if (type.equals(Constants.POST)) {
            Optional<PostLike> optionalLikeDto = Optional
                    .ofNullable(postLikeRepository.getPersonPostLike(securityPerson.getPersonId(), itemId));
            if (optionalLikeDto.isEmpty()) {
                postLikeRepository.addLikeToPost(securityPerson.getPersonId(), itemId);
                LikesDto likesDto = getLikes(itemId, Constants.POST);
                postService.updatePostLikeCount(likesDto.getLikes(), itemId);
                return likesDto;
            }
            return getLikes(itemId, Constants.POST);
        } else if (type.equals(Constants.COMMENT)) {
            Optional<CommentLike> optionalLikeDto = Optional
                    .ofNullable(commentLikeRepository.getPersonLike(securityPerson.getPersonId(), itemId));
            if (optionalLikeDto.isEmpty()) {
                commentLikeRepository.addLike(securityPerson.getPersonId(), itemId);
                LikesDto likesDto = getLikes(itemId, Constants.COMMENT);
                postService.updateCommentLikeCount(likesDto.getLikes(), itemId);
            }
            return getLikes(itemId, Constants.COMMENT);
        }
        throw new InvalidRequestException(ExceptionText.LIKE_WRONG_TYPE.getMessage());

    }

    public LikedDto getLiked(Integer itemId, String type) throws InvalidRequestException {
        int personId = securityPerson.getPersonId();
        if (type.equals(Constants.POST)) {
            return new LikedDto(postLikeRepository.getIsPostLiked(personId, itemId));
        } else if (type.equals(Constants.COMMENT)) {
            return new LikedDto(commentLikeRepository.getIsLiked(personId, itemId));
        }
        throw new InvalidRequestException(ExceptionText.LIKE_WRONG_TYPE.getMessage());
    }

    public LikesDto deleteLike(int itemId, String type) throws InvalidRequestException {
        LikesDto likesDto = new LikesDto();
        if (type.equals(Constants.POST)) {
            postLikeRepository.deleteLikeFromPost(securityPerson.getPersonId(), itemId);
            List<PostLike> likeList = postLikeRepository.getPostLikes(itemId);
            likesDto.setLikes(likeList.size());
            likesDto.setUsers(likeList.stream().map(PostLike::getPersonId).collect(Collectors.toList()));
            postService.updatePostLikeCount(likesDto.getLikes(), itemId);
            return likesDto;
        } else if (type.equals(Constants.COMMENT)) {
            commentLikeRepository.deleteLike(securityPerson.getPersonId(), itemId);
            List<CommentLike> likeList = commentLikeRepository.getLikes(itemId);
            likesDto.setLikes(likeList.size());
            likesDto.setUsers(likeList.stream().map(CommentLike::getPersonId).collect(Collectors.toList()));
            postService.updateCommentLikeCount(likesDto.getLikes(), itemId);
            return likesDto;
        }
        throw new InvalidRequestException(ExceptionText.LIKE_WRONG_TYPE.getMessage());
    }
}
