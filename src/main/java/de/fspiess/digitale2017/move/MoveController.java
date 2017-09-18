package de.fspiess.digitale2017.move;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoveController {
	
	//make automatically generated MoveService instance available to MoveController:
	@Autowired
	private MoveService moveService;
	
	/*
	 * default: GET request
	 */
	@RequestMapping("/moves")
	public List<Move> getAllMoves(){
		return moveService.getAllMoves();
	}
	
	@RequestMapping("/moves/{id}")
	public Move getMove(@PathVariable String id){
		return moveService.getMove(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/moves")
	public void addMove(@RequestBody Move move){
		moveService.addMove(move);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/moves/{id}")
	public void updateMove(@RequestBody Move move, @PathVariable String id){
		moveService.updateMove(id, move);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/moves/{id}")
	public void deleteMove(@PathVariable String id){
		moveService.deleteMove(id);
	}
	
	public MoveService getMoveService(){
		return moveService;
	}

}
