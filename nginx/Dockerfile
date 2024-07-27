FROM nginx:alpine
MAINTAINER paulo
EXPOSE 8080
RUN rm /etc/nginx/nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

#####################################################
# The JAVA ROUTER-SERVLET-APPLICATION               #
# is not necessary IN THIS CASE,                    #
# because NGINX will be the                         #
# router(reverse-proxy + load balancing).           #
#                                                   #
# Therefore, all staff used (such as maven,         #
# tomcat, tomcat environment variables, etc...)     #
# for the JAVA ROUTER-SERVLET-APPLICATION           #
# (incluiding that) is not necessary anymore        #
#                                                   #
# because ALL of them was substituted for NGINX     #
#####################################################
# use-case 05
#FROM pictolearn/tomcat8-jdk8

#install maven for debug
#RUN apt-get -y install maven lsof

#Set TOMCAT: Catalina HOME and JAVA_OPTS
#ENV CATALINA_BASE /opt/tomcat
#ENV CATALINA_HOME /opt/tomcat
#ENV PATH $PATH:$CATALINA_HOME/bin
#ENV JAVA_OPTS="-Xms1024m -Xmx1024m -Xss8192k -XX:PermSize=500m -XX:CMSInitiatingOccupancyFraction=50 -XX:+ExplicitGCInvokesConcurrent -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:NewRatio=1 -XX:SurvivorRatio=1  -Dorg.apache.cxf.JDKBugHacks.imageIO=false"
#
#EXPOSE 8080
#EXPOSE 8009
#WORKDIR /opt/tomcat

#General procedures
#ADD . /usr/local/router
#RUN mkdir -p /usr/local/router/logs
#
#RUN cd /usr/local/router && mvn clean package
#
#RUN  cp /usr/local/router/target/router-shield.war /opt/tomcat/webapps/router-shield.war

#run TOMCAT
#CMD ["/opt/tomcat/bin/catalina.sh","run"]