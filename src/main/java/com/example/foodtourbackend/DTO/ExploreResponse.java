package com.example.foodtourbackend.DTO;

import java.util.List;

public class ExploreResponse {
  public String title;
  public List<DishesResponse> dishesResponseList ;

  public ExploreResponse() {}
  public ExploreResponse(String title, List<DishesResponse> dishesResponseList) {
    this.title = title;
    this.dishesResponseList = dishesResponseList;
  }
  public static class DishesResponse {
    public String name;
    public String description;
    public DishesResponse() {}

    public DishesResponse(String name, String description) {
      this.name = name;
      this.description = description;
    }
    public String getName() {
      return name;
    }

    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public void setName(String name) {
      this.name = name;
    }

  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public List<DishesResponse> getDishesResponseList() {
    return dishesResponseList;
  }
  public void setDishesResponseList(List<DishesResponse> dishesResponseList) {
    this.dishesResponseList = dishesResponseList;
  }

}
