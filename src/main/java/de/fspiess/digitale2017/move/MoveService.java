package de.fspiess.digitale2017.move;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * idea of lesson #17: the output of the list has now been moved to a service!
 */

@Service
public class MoveService {
	
	@Autowired
	private MoveRepository moveRepository;
	
	//new ArrayList<>(...) makes sure that this list is mutable, i.e. adding entries via POST is allowed:
	/*private List<Move> moves= new ArrayList<>(Arrays.asList(
			new Move("Spring", "Spring Framework", "SF Description"),
			new Move("Java", "Core Java", "Core Java Description"),
			new Move("javascript", "JavaScript","JavaScript Description")
			));*/
	
	public List<Move> getAllMoves() {
		//return moves;
		List<Move> moves = new ArrayList<>();
		moveRepository.findAll() //this returns an iterable
		.forEach(moves::add);//call add method to add each iterabel to list. check out Java 8 Lambda Basis course
		return moves;
	}
	
	/*
	 * attention, we have a lambda expression here to iterate over the array:
	 */
	public Move getMove(String id){
		//return moves.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return moveRepository.findOne(id);
	}

	public void addMove(Move move) {
		//moves.add(move);
		moveRepository.save(move);
	}

	//this time, we do not use lambda, but we iterate in a for loop:
	public void updateMove(String id, Move move) {
		/*for (int i=0; i<moves.size(); i++){
			Move t = moves.get(i);
			if (t.getId().equals(id)){
				moves.set(i, move);
				return;
			}
		}*/
		moveRepository.save(move);
	}

	public void deleteMove(String id) {
		//moves.removeIf(t -> t.getId().equals(id));
		moveRepository.delete(id);
	}

}