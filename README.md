This is a project for hand-written, slow-coding through the Spring AI codebase to ensure a full grasp of the ecosystem.

The key challenge: Create a "Dating Profile Assistant". The quirk? Make it as weird as possible by inlcuding the weirdest data source possible, and repeating on top of it over and over.

The especially focal point is to make it as local-runnable as possible; no external DBs, no external API, but running LM Studio, local DBs, and ideally local AWS Data Sources.

Step 1: AI Based response API to assist a user in creating dating profile details.

Step 2: File-to-Redis based RAG to pull data in

Step 3: Create Agentic toolchain to perform writing commands using RAG data

Step 4: Use local AWS emulator to simulate cloud sources; S3, DynamoDB, and RDS

Step 5: Create a React/Thymeleaf UI to present these features (may use a generator to quickly get a template...this isn't a React project YYET)

Step 6: Create Agentic toolchain to process requests similar to step 3, and perform external research before writing

Step 7: Create a text-to-image endpoint that alllows users to generate the shenanigans we're doing

Notes for running this in local in the future:

- Start Redis Docker Container
- Start model google/gemma-4-12b-qat (or modify in app.properties)
- Start model text-embedding-nomic-embed-text-v1.5 (or modify app.properties)
- Replace "Sample_Data-1.pdf" with any goofy story desired
- (Currently about Skiing, LOTR, and Ninja Turtles)
- A sample REST collection is saved under resources