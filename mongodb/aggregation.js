db.getCollection("ingestions").aggregate(

	// Pipeline
	[
		// Stage 1
		{
			$group: {
			  _id: {
			       	year : { $year : "$time" },        
			        month : { $month : "$time" },        
			        day : { $dayOfMonth : "$time" }
			    },
			  total: {
			    $sum: "$calories"
			  }
			}
		},

	]

	// Created with Studio 3T, the IDE for MongoDB - https://studio3t.com/

);
