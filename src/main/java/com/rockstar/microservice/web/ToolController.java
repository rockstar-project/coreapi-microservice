package com.rockstar.microservice.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rockstar.microservice.domain.Tool;
import com.rockstar.microservice.domain.ToolRepository;

@RestController
@RequestMapping("/tools")
public class ToolController {
	
    @Autowired
    private ToolRepository toolRepository;
    
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Tool>>> findAll() {
    		List<Resource<Tool>> tools = StreamSupport.stream(toolRepository.findAll().spliterator(), false)
    			.map(tool -> new Resource<>(tool,
    				linkTo(methodOn(ToolController.class).findOne(tool.getId())).withSelfRel()))
    			.collect(Collectors.toList());

    		return ResponseEntity.ok(
    			new Resources<>(tools,linkTo(methodOn(ToolController.class).findAll()).withSelfRel()));
    }
    
    @PostMapping
	public ResponseEntity<?> newTool(@Valid @RequestBody Tool tool) {
    		Tool savedTool = null;
    		
		try {
			savedTool = this.toolRepository.save(tool);
			Link newlyCreatedLink = linkTo(methodOn(ToolController.class).findOne(savedTool.getId())).withSelfRel();

			Resource<Tool> toolResource = new Resource<>(savedTool,
				linkTo(methodOn(ToolController.class).findOne(savedTool.getId())).withSelfRel());

			return ResponseEntity
				.created(new URI(newlyCreatedLink.getHref()))
				.body(toolResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + tool);
		}
    }
    
    @GetMapping(value = "/{slug}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Tool>> findOne(@PathVariable String slug) {

    		return this.toolRepository.findById(slug)
    			.map(tool -> new Resource<>(tool,
    				linkTo(methodOn(ToolController.class).findOne(tool.getId())).withSelfRel()))
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{slug}")
	public ResponseEntity<?> updateTool(@RequestBody Tool tool, @PathVariable String slug) {
		Tool toolToUpdate = tool;
		toolToUpdate.setId(slug);
		this.toolRepository.save(toolToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(ToolController.class).findOne(slug)).withSelfRel();

		try {
			return ResponseEntity.noContent()
				.location(new URI(newlyCreatedLink.getHref()))
				.build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + toolToUpdate);
		}
    }
    
    @DeleteMapping("/{slug}")
	public ResponseEntity<?> deleteTool(@PathVariable String slug) {
		this.toolRepository.deleteById(slug);
		return ResponseEntity.noContent().build();
    }
    
}