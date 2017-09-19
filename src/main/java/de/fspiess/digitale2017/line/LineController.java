package de.fspiess.digitale2017.line;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {
	
	//make automatically generated LineService instance available to LineController:
	@Autowired
	private LineService lineService;
	
	/*
	 * default: GET request
	 */
	@RequestMapping("/lines")
	public List<Line> getAllLines(){
		return lineService.getAllLines();
	}
	
	@RequestMapping("/lines/{id}")
	public Line getLine(@PathVariable String id){
		return lineService.getLine(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/lines")
	public void addLine(@RequestBody Line line){
		lineService.addLine(line);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/lines/{id}")
	public void updateLine(@RequestBody Line line, @PathVariable String id){
		lineService.updateLine(id, line);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/lines/{id}")
	public void deleteLine(@PathVariable String id){
		lineService.deleteLine(id);
	}
	
	public LineService getLineService(){
		return lineService;
	}

}
