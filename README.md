# bugsfree

##TO RUN EmailSender Microservice

###STEP 1 = Cd emailsender (move into emailsender directory where you can see dockerfile)
###STEP 2 = docker build -t emailsender-v1 .  (run this you can replace "emailsender-v1" with custom name)
###STEP 3 = docker run -it -e "SERVER_PORT=61002" -p 61002:61002 emailsender-v1 (you can use same name or img id at "emailsender-v1")
