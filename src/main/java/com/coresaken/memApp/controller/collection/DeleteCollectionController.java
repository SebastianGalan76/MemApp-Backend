package com.coresaken.memApp.controller.collection;

import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.service.collection.DeleteCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteCollectionController {
    final DeleteCollectionService deleteCollectionService;

    @DeleteMapping("/collection/{id}")
    private ResponseEntity<Response> delete(@PathVariable("id") Long id){
        return deleteCollectionService.delete(id);
    }
}
