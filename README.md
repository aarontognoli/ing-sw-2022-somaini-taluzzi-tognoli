# Prova Finale Ingegneria del Software

The goal of the project was to implement on software (both CLI and GUI)
the [Eriantys](https://www.craniocreations.it/prodotto/eriantys/) table game.

## Authors

1. Andrea Somaini
2. Agnese Taluzzi
3. Aaron Tognoli

## What was implemented

- [x] Complete Rules
- [x] TCP Sockets
- [x] CLI
- [x] GUI
- [x] Additional Functionality
  - [x] Implemented every character card
  - [x] 4 Players games
  - [x] Multiple games in a single server instance

## Testing

We wrote several tests to check that the behaviour of our code is correct.

We focussed on testing the core features of the game engine: the Model and the classes it uses, the Controller and
networking.

### Excluded from coverage

- mvc.view.*
- mvc.model.CLIModelPrinter
- messages.*
- ClientApp
- ServerApp
- client.Client

### Report

| Name       | Class %      | Method %      | Line %          |
|------------|--------------|---------------|-----------------|
| Complete   | 92% (70/76)  | 82% (254/308) | 83% (1074/1292) |
| bag        | 100% (1/1)   | 100% (5/5)    | 100% (19/19)    |
| cards      | 100% (21/21) | 74% (59/79)   | 84% (253/300)   |
| cloud      | 100% (1/1)   | 75% (3/4)     | 85% (6/7)       |
| enums      | 83% (5/6)    | 83% (10/12)   | 87% (21/24)     |
| exceptions | 75% (12/16)  | 70% (12/17)   | 73% (14/19)     |
| mvc        | 94% (17/18)  | 80% (83/103)  | 80% (485/604)   |
| notifier   | 100% (1/1)   | 100% (3/3)    | 100% (6/6)      |
| pawn       | 100% (4/4)   | 92% (13/14)   | 95% (22/23)     |
| places     | 100% (1/1)   | 100% (12/12)  | 91% (42/46)     |
| player     | 100% (2/2)   | 96% (24/25)   | 91% (51/56)     |
| server     | 100% (5/5)   | 88% (30/34)   | 82% (155/188)   |

## How to run

### Server

`java -jar deliverables/server.jar`

### Client

`java -jar deliverables/client.jar <client_type> <server_ip>`

Where:

1. `client_type` must be `cli` or `gui`
2. `server_ip` is the IP of the server 
