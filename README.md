# parallel-expression-evaluator

Parallel evaluation of arithmetic expression based on Akka actors

Example:
$ curl -H " Content-Type: application/json " \ -X POST \
-d ' {"expression":"(1-1)*2+3*(1-3+4)+10/2"} ' \ http: //localhost:5555/evaluate
{" result ": 11}
