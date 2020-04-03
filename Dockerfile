FROM adoptopenjdk/openjdk11:ubi
RUN groupadd spring && adduser spring -g spring
USER spring:spring
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","co.uk.coronavirus.CoronaVirusApplication"]
