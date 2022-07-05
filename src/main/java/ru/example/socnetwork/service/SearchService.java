package ru.example.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.socnetwork.logging.DebugLogs;
import ru.example.socnetwork.model.entity.City;
import ru.example.socnetwork.model.entity.Country;
import ru.example.socnetwork.repository.CityCountryRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@DebugLogs
public class SearchService {

  private final CityCountryRepository cityCountryRepository;

  public List<String> getCountryList() {
    return cityCountryRepository
            .getCountryList()
            .stream()
            .map(Country::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }

  public List<String> getCityList() {
    return cityCountryRepository
            .getCityList()
            .stream()
            .map(City::getName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }
}
