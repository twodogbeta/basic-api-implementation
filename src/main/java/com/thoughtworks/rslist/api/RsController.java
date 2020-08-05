package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController

public class RsController {

  private static final List<RsEvent> rsList = new ArrayList<>();

  public static void  initRsEvent(){
    rsList.clear();
    User user = new User("Tom", "male", 40, "tom@gmail.com", "15800000000");
    rsList.add(new RsEvent("第一条事件","无分类",user));
    rsList.add(new RsEvent("第二条事件","无分类",user));
    rsList.add(new RsEvent("第三条事件","无分类",user));
  }


  @GetMapping("/rs/list/{index}")
  public RsEvent getOneRsEvent(@PathVariable int index){
    return rsList.get(index - 1);
  }
  @GetMapping("/rs/list")
  public List<RsEvent> getList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end){
    if (start == null && end == null){
      return rsList;
    }
    return rsList.subList(start - 1,end);
  }
  @PostMapping("/rs/list")
  public void addOneRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
  }

  @PutMapping("rs/list/{index}")
  public void modifyOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    rsList.set(index - 1, rsEvent);
  }
  @DeleteMapping("rs/list/{index}")
  public void deleteOneRsEvent(@PathVariable int index) {
    rsList.remove(index - 1);
  }
  @PostMapping("rs/justadd")
  void justAddRsEventWhenHasUser(@RequestBody @Valid RsEvent rsEvent){
      rsList.add(rsEvent);
      UserController.register(rsEvent.getUser());
  }
}



