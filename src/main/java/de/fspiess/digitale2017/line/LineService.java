package de.fspiess.digitale2017.line;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * idea of lesson #17: the output of the list has now been lined to a service!
 */

@Service
public class LineService {
	
	@Autowired
	private LineRepository lineRepository;
	
	//new ArrayList<>(...) makes sure that this list is mutable, i.e. adding entries via POST is allowed:
	/*private List<Line> lines= new ArrayList<>(Arrays.asList(
			new Line("Spring", "Spring Framework", "SF Description"),
			new Line("Java", "Core Java", "Core Java Description"),
			new Line("javascript", "JavaScript","JavaScript Description")
			));*/
	
	public List<Line> getAllLines() {
		//return lines;
		List<Line> lines = new ArrayList<>();
		lineRepository.findAll() //this returns an iterable
		.forEach(lines::add);//call add method to add each iterabel to list. check out Java 8 Lambda Basis course
		return lines;
	}
	
	/*
	 * attention, we have a lambda expression here to iterate over the array:
	 */
	public Line getLine(String id){
		//return lines.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		return lineRepository.findOne(id);
	}

	public void addLine(Line line) {
		//lines.add(line);
		lineRepository.save(line);
	}

	//this time, we do not use lambda, but we iterate in a for loop:
	public void updateLine(String id, Line line) {
		/*for (int i=0; i<lines.size(); i++){
			Line t = lines.get(i);
			if (t.getId().equals(id)){
				lines.set(i, line);
				return;
			}
		}*/
		lineRepository.save(line);
	}

	public void deleteLine(String id) {
		//lines.relineIf(t -> t.getId().equals(id));
		lineRepository.delete(id);
	}

}