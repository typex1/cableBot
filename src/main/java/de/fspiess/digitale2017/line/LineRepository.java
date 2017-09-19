package de.fspiess.digitale2017.line;

import org.springframework.data.repository.CrudRepository;

//the data layer does not need to be implemented!
//from docs.spring.io: "The CrudRepository provides CRUD functionality for the entity class that is being managed."
//CrudRepository contains the logic for any entity class! Entity class = Topic, ID = String.
public interface LineRepository extends CrudRepository<Line, String>{
	
	// already contained in CrudRepository:
	// you only have to implement the SPECIAL stuff you need.
	// getAllTopics()
	// getTopic(String id)
	// updateTopic(Topic t)
	// deleteTopic(String id)
	
}
