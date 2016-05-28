# gossipSimulator
A simulator for the gossip algorithm

this project simulate a distibuted system.<br>
when running RunNodes.java, 5 process(Node.jar) starts running at the same time.<br>
every process acts like a node in a distribute system.<br>
that means he is indepentes and there is no managment system that manage all nodes.<br><br>

Node.java implement the gossip algorithem:<br>
every node has an id(port number on the machine), heartbit(how long he is alive), node value and a table of data(other nodes in system).<br>
when the node start running:<br>
1.a cmd opens for him.<br>
2.he is trying to connect to others. saying he is alive.<br>
3.he is waiting for a msg from another node OR value from the user.<br><br>

when a node is getting a msg OR a new value from the user he is randomly pick one other node from his table and send him his data.<br>
the node that gets that data do the same and at the end the data spread in the system without a mangament system.<br><br>

when data come to a node he is cheacking if he need to change it by the heartbit of the node he is willing to change,<br>
if in his data the hearbit is bigger then the heartbit he got, then the data is old and he dont need to change it.<br><br>

optional values the user can press as input:<br>
1.value for node(0-9)<br>
2.print table of data(11)<br>
3.shutdown node(00)<br><br>

*need to be implemented:<br>
a detector for a shutdown node, that will delete a dead node from table of nodes.
