# gossipSimulator
A simulator for the gossip algorithm

this project simulate a distibuted system.
when running RunNodes.java, 5 process(Node.jar) starts running at the same time.

every process acts like a node in a distribute system.

that means he is indepentes and there is no managment system that manage all nodes.

Node.java implement the gossip algorithem:
every node has an id(port number on the machine), heartbit(how long he is alive), node value and a table of data(other nodes in system).
when the node start running:
1. a cmd opens for him.
2.he is trying to connect to others. saying he is alive.
3.he is waiting for a msg from another node OR value from the user.

when a node is getting a msg OR a new value from the user he is randomly pick one other node from his table and send him his data.
the node that gets that data do the same and at the end the data spread in the system without a mangament system.

when data come to a node he is cheacking if he need to change it by the heartbit of the node he is willing to change,
if in his data the hearbit is bigger then the heartbit he got, then the data is old and he dont need to change it.

optional values the user can press as input:
1.value for node(0-9)
2.print table of data(11)
3.shutdown node(00)

*need to be implemented:
a detector for a shutdown node, that will delete a dead node from table of nodes.
