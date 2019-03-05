# Bagual Shortening Service

## Descrição / Description

**pt-br**

O bagu.al é um encurtador de urls que nasceu no dia 20 de setembro de 2011 como um pet project feito em um dia 
incentivado por uma amiga.
Inicialmente construído para ser uma plataforma de encurtadores como serviço, a aplicação passou por diversas alterações 
acabando por ser foi totalmente reescrita para atender somente o domínio **bagu.al**.

Atualmente oferece como funcionalidades o encurtamento de urls pela [página web](https://bagu.al), 
plugin para o [Google Chrome](http://bagu.al/BP) e visualização de informações para cada url encurtada 
(bastando adicionar um  *+* no final do link encurtado. Ex.: [https://bagu.al/A+](https://bagu.al/A+) ).

**en-us**

The bagu.al is a urls shortener who was born on September 20, 2011 as a pet project done on a day encouraged by a friend. Initially built to be a shortening platform as a service, the application went through several changes eventually being completely rewritten to serve only the domain **bagu.al**.

Currently offers as features the shortening of urls by the [web page](https://bagu.al), plugin for [Google Chrome](http://bagu.al/BP) and information visualization for each shortened url (simply add a *+* at the end of the shortened link, eg. [https://bagu.al/A+](https://bagu.al/A+)).

## Tecnologias / Tech Stack

- Java 8
- Spring Boot
- Hibernate
- Lombok
- Angular
- Gradle
- Docker
- MySql

## Rodando localmente / Run locally

**pt-br**

Para rodar na máquina local você precisa ter instalado os seguintes softwares:

- docker-compose: https://docs.docker.com/compose/install/
- Java JDK 8: https://openjdk.java.net/install/

Satisfazendo essas condições, basta rodar `make run-local` no terminal. Esse target executará o docker-compose subindo um banco mysql e o mountebank (http://www.mbtest.org/) para simular o serviço anti-phishing.

**en-us**

To run on the local machine you need to have installed the following software:

- docker-compose: https://docs.docker.com/compose/install/
- Java JDK 8: https://openjdk.java.net/install/

By satisfying these conditions, simply run `make run-local` in the terminal. This target will run docker-compose starting a mysql database and mountebank (http://www.mbtest.org/) to simulate the anti-phishing service.

## Como contribuir / How to contribute

**pt-br**

Você pode participar do projeto sugerindo algo legal, criando uma issue, abrindo um pull request
ou até mesmo [pagando um café](https://pag.ae/7UFskKU4n) = )

No momento o deploy é feito por mim mesmo, mas futuramente será automatizado.

**en-us**

You can join the project by suggesting something cool, creating an issue, opening a pull request
or even [paying me a coffee](https://pag.ae/7UFskKU4n) =)

At the moment deploy is done by myself, but in the future it will be automated.

___

Made with <span style="color: #e25555;">&hearts;</span> by [@maedabr](https://twitter.com/maedabr)
