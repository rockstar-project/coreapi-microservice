package com.rockstar.microservice.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rockstar.microservice.domain.Runtime;
import com.rockstar.microservice.service.RuntimeService;

@CrossOrigin
@RestController
public class RuntimeController {
	
	@Autowired
    private RuntimeService runtimeService;
    
    @PostMapping("/runtimes")
    public ResponseEntity<Runtime> create(@Valid @RequestBody Runtime runtime, UriComponentsBuilder uriBuilder) throws Exception {
    		Runtime newRuntime = runtimeService.createRuntime(runtime);
        return ResponseEntity.created(uriBuilder.path("/runtimes/{id}").buildAndExpand(newRuntime.getId()).toUri()).body(newRuntime);
    }

    @GetMapping("/runtimes")
    public ResponseEntity<Collection<Runtime>> findAll(){
        return ResponseEntity.ok(runtimeService.getRuntimes());
    }

    @GetMapping("/runtimes/{id}")
    public ResponseEntity<Runtime> show(@PathVariable String id){
        return ResponseEntity.ok(runtimeService.getRuntime(id));
    }

    @PatchMapping("/runtimes/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Runtime runtime) {
    		runtime.setId(id);
        runtimeService.updateRuntime(runtime);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/runtimes/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		runtimeService.deleteRuntime(id);
        return ResponseEntity.noContent().build();
    }
    
}