package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index - 1));
  }
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end){
    if (start == null && end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start - 1,end));
  }
  @PostMapping("/rs/event")
  public ResponseEntity addOneRsEvent(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PutMapping("rs/list/{index}")
  public ResponseEntity modifyOneRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) {
    return ResponseEntity.ok(rsList.set(index - 1, rsEvent));
  }
  @DeleteMapping("rs/list/{index}")
  public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
   return ResponseEntity.ok(rsList.remove(index - 1));
  }
  @PostMapping("rs/justadd")
  public ResponseEntity justAddRsEventWhenHasUser(@RequestBody @Valid RsEvent rsEvent){
      rsList.add(rsEvent);
      UserController.register(rsEvent.getUser());
     return ResponseEntity.ok().build();
  }
}



