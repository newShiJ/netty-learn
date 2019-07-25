from netty.rpc.thrift.py import PersonService
from netty.rpc.thrift.py import ttypes

from thrift import Thrift
from thrift.transport import TSocket
from thrift.protocol import TCompactProtocol
from thrift.transport import TTransport

try:
    tSocket = TSocket.TSocket('127.0.0.1',8899)
    tSocket.setTimeout(600)

    transport = TTransport.TFramedTransport(tSocket)
    protocol = TCompactProtocol.TCompactProtocol(transport)
    client = PersonService.Client(protocol)

    transport.open()

    person = client.getPersonByName("xxxx")
    print (person)
    client.savePerson(person)
    
    transport.close()

except Thrift.TException as e:
    print (e)