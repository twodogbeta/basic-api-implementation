package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CommonError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.utils.ListOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController

public class RsController {

    public static List<RsEvent> rsList = new ArrayList<>();


    public static void initRsEvent() {
        rsList.clear();
        UserController.userList.clear();
        User user1 = new User("pop", "male", 40, "tom@gmail.com", "15800000000");
        rsList.add(new RsEvent("第一条事件", "无分类", user1));
        rsList.add(new RsEvent("第二条事件", "无分类", user1));
        rsList.add(new RsEvent("第三条事件", "无分类", user1));
        UserController.userList.add(user1);
    }


    @GetMapping("/rs/list/{index}")
    public ResponseEntity getOneRsEvent(@PathVariable int index) throws InvalidIndexException {
        if (index  > rsList.size()){
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"invalid index\"}");
            throw new InvalidIndexException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getList(@RequestParam(required = false) Integer start,
                                                 @RequestParam(required = false) Integer end) throws InvalidIndexException {


            if (ListOperation.isLegalIndex(start,end,rsList))
                return ResponseEntity.ok(rsList.subList(start - 1, end));
            else if (start == null && end == null){
                return ResponseEntity.ok(rsList);
            }
            throw new InvalidIndexException("invalid request param");

    }

    @PostMapping("/rs/event")
    public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        for (User u : UserController.userList) {
            if (u.getName().equals(rsEvent.getUser().getName())) {
                rsList.add(rsEvent);
                String headers = String.valueOf(rsList.indexOf(rsEvent));
                return ResponseEntity.created(URI.create(headers)).build();
            }
        }
        rsList.add(rsEvent);
        UserController.userList.add(rsEvent.getUser());
        String headers = String.valueOf(rsList.indexOf(rsEvent));
        return ResponseEntity.created(URI.create(headers)).build();
    }

    @PutMapping("rs/list/{index}")
    public ResponseEntity modifyOneRsEvent(@PathVariable int index, @RequestBody @Validated RsEvent rsEvent) {
        return ResponseEntity.ok(rsList.set(index - 1, rsEvent));
    }

    @DeleteMapping("rs/list/{index}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.remove(index - 1));
    }

    @ExceptionHandler({InvalidIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler (Exception ex){
        String errorMessage ;
        CommonError commonError = new CommonError();
        if (ex instanceof  MethodArgumentNotValidException)
            errorMessage  = "invalid param";
        else  errorMessage = ex.getMessage();
        commonError.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }


/*    @ExceptionHandler(InvalidIndexException.class)
    public ResponseEntity exceptionHandler (InvalidIndexException ex){
        CommonError commonError = new CommonError();
        commonError.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity exceptionHandler1 (MethodArgumentNotValidException ex){
        CommonError commonError = new CommonError();
        commonError.setError("invalid param");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonError);
    }*/

}



