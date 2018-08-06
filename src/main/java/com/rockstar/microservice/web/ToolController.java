package com.rockstar.microservice.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rockstar.microservice.domain.Tool;
import com.rockstar.microservice.service.ToolService;

@RestController
public class ToolController {
	
	@Autowired
    private ToolService toolService;
    
    @PostMapping("/tools")
    public ResponseEntity<Tool> create(@Valid @RequestBody Tool tool, UriComponentsBuilder uriBuilder) throws Exception {
    		Tool newTool = toolService.createTool(tool);
        return ResponseEntity.created(uriBuilder.path("/tools/{id}").buildAndExpand(newTool.getId()).toUri()).body(newTool);
    }

    @GetMapping("/tools")
    public ResponseEntity<Collection<Tool>> findAll(){
        return ResponseEntity.ok(toolService.getTools());
    }

    @GetMapping("/tools/{id}")
    public ResponseEntity<Tool> show(@PathVariable String id){
        return ResponseEntity.ok(toolService.getTool(id));
    }

    @PatchMapping("/tools/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Tool tool) {
    		tool.setId(id);
        toolService.updateTool(tool);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tools/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
    		toolService.deleteTool(id);
        return ResponseEntity.noContent().build();
    }
    
}