define(['scripts/d3.min', 'scripts/elasticsearch'], function (d3, elasticsearch) {
    "use strict";
    var client = new elasticsearch.Client({
                  host: 'localhost:9200',
                  log: 'trace'
    });
    client.search({
  	index: 'houseprice_numbers',
  	body: {
    		query: {
      			filtered: {
        		  filter: {
        			  bool:{
        				  should:[
        				          {term:{price:154000}},
        				          {term:{city:'Helsinki'}}
        				  }
        				          ]
        			  }
        		  }
                },
                aggs: {
                     "price_stats" : { "stats" : { "field" : "price" } }
                }
               }
    }
  }
}).then(function (resp) {
    var hits = resp.hits.hits;
    console.log(hits);
}, function (err) {
    console.trace(err.message);
});
});