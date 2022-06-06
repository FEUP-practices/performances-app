FROM gradle:7.2-jdk11
WORKDIR /app
COPY . /app 
# Get permissions to execute build            
USER root 
RUN chown -R gradle /app
USER gradle              
RUN ./gradlew --stop
RUN ./gradlew build -x test
CMD ["./gradlew", "bootRun"]