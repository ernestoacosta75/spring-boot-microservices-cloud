FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/configserver
RUN echo $JAVA_HOME
# download Java Cryptography Extension
RUN cd /tmp/ && \
    #wget "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" --header 'Cookie: oraclelicense=accept-securebackup-cookie' && \
    curl -k -LO "http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip" -b 'oraclelicense=accept-securebackup-cookie' && \
    unzip jce_policy-8.zip && \
    #rm /tmp/jce_policiy-8.zip && \
    yes | cp -v /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/

RUN cd /tmp/ && rm jce_policy-8.zip
ADD  @project.build.finalName@.jar /usr/local/configserver/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
