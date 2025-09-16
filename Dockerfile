FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache git-crypt git

WORKDIR /app

ARG GIT_CRYPT_KEY

RUN if [ -z "$GIT_CRYPT_KEY" ]; then echo "ERROR: GIT_CRYPT_KEY not provided"; exit 1; fi

COPY . ./

RUN git init .
RUN git add .
RUN git -c user.email="build@railway.com" -c user.name="Railway Build" commit -m "Initial commit" || echo "Commit failed, continuing..."

RUN echo "$GIT_CRYPT_KEY" | base64 -d > ./git-crypt-key
RUN ls -la ./git-crypt-key
RUN git-crypt unlock ./git-crypt-key

RUN echo "Checking database files after unlock:"
RUN find . -name "*.db" -exec ls -la {} \;
RUN find . -name "*.db" -exec file {} \;

RUN rm ./git-crypt-key

RUN chmod +x ./mvnw

RUN ./mvnw -DoutputFile=target/mvn-dependency-list.log -B -DskipTests clean dependency:list install

CMD ["sh", "-c", "java -jar target/*.jar"]