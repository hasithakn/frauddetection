```plantuml
title Highlevel

[SOLR INDEXER]
[SOLR SEARCHER]
[DB SUMMARY]

database "mysql_clients_DB" {
folder "Main message DB" {
	[message_history]
  }
}


database "SOLR" {
folder "solr core" {
	[solr_message_history]
  }
}

database "mysql1" {
folder "solr_index_details_db" {
	[solr_index_details]
  }
}
note left 
Status : FINISHED/STARTED
Date
current_doc
end note

database "mysql2" {
folder "solr_search_details_db" {
	[solr_search_details]
  }
}
note left 
Status : FINISHED/STARTED
Date
current_doc
end note


database "mysql3" {
folder "detailed_duplicate_details_db" {
	[detailed_duplicate_details]
  }
}
note right 
correlation_id
d1
d7
d30
app_id
date
end note

database "mysql4" {
folder "daily_analysis_summery_db" {
	[daily_analysis_summery]
  }
}
note left
id
duplicateScore
date
app_id
end note

[SOLR INDEXER] <-up- [message_history]
[SOLR INDEXER] -left-> [solr_message_history]
[SOLR INDEXER] -right-> [solr_index_details]

[solr_message_history] --> [SOLR SEARCHER] 
[SOLR SEARCHER] <-- [solr_index_details]
[SOLR SEARCHER] <-- [solr_search_details]
[SOLR SEARCHER] -right-> [detailed_duplicate_details]

[DB SUMMARY] -up-> [detailed_duplicate_details]
[DB SUMMARY] --> [daily_analysis_summery]


```