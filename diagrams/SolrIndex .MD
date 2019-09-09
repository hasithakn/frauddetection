```plantuml

title Solr Indexing

(*) --> "Check solr_index_details for rows Until today" 
If [for each] found then

-left->[yes] if "check status \n == FINISHED" then
-right->[yes] "Already Finished Thus Skip"
else
-down-> [NO] "Get current doc and  \n start from that place"
endif

else
--> [NO] "add new row and set status \n as STARTED"
Endif

"add new row and set status \n as STARTED" --> "Get batchSize and currentDoc"

"Get current doc and  \n start from that place" -right-> "Get batchSize and currentDoc"
"Get batchSize and currentDoc" --> "read docs from mysql and index to SOLR"

if "Finished indexing all" then
-down->[Yes] "Update solr_index_details row finished"
else
-left-> [NO] "Update solr_index_details rwo with \n current_doc = current_doc +batch "
endif
"Update solr_index_details rwo with \n current_doc = current_doc +batch " --> "Get batchSize and currentDoc"

```
