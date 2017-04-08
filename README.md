## Commit Messages
https://chris.beams.io/posts/git-commit/

## Gitflow
We use gitflow for releasing. View documentation at http://danielkummer.github.io/git-flow-cheatsheet/

## Vagrant
Vagrantfile is defined in the project's root folder.

To check the status of the vagrant boxes defined in the file `vagrant status`

To bring up an individual box eg. the services box `vagrant up services`

## Consul
We are using Consul for service discovery using the docker image https://hub.docker.com/r/progrium/consul/

To pull the latest image `docker pull progrium/consul`

To check the installation manually `docker run -d -p 8500:8500 progrium/consul -server -bootstrap -ui-dir ui` and then go to `localhost:8500`

For local testing consul server will be deployed within the services vagrant box. This will be started with the `vagrant up services` command

The ui can then be acceessed at the {static-ip}:8500/ui where the {static-ip} is the ip defined in the vagrant file for the services box

Joining the cluster can be tested manually by running the following command from the terminal, which will connect the consul client to the server

`docker run -d -p 8300:8300 -p 8301:8301 -p 8301:8301/udp -p 8302:8302 -p 8302:8302/udp -p 8400:8400 -p 8500:8500 progrium/consul -advertise 127.0.0.1 -join {static-ip}` 
where the {static-ip} is the ip defined in the vagrant file for the services box


## LOGSTASH
If enabled in the configuration, Logstash with ES/Kibana will be used for logging.

For local testing, elastic search and kibana will be installed on the `services` box

With the services box running, kibana can be accessed at `http://{static-ip}:5601/`

To test locally, enable logstash in the configuration, and start up a local docker container running logstash which will 
forward the logs to the elastic search server running on the `services` vagrant box

`docker run -p 51515:51515 logstash -e 'input { tcp { port => 51515 codec => json_lines } } output { elasticsearch { hosts => ["192.168.1.10"]} }'`

-e flag allows us to specify the configuration as part of the command