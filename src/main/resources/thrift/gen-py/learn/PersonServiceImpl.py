
from netty.rpc.thrift.py import PersonService
from netty.rpc.thrift.py import ttypes

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TCompactProtocol
from thrift.server import TServer

class PersonServiceImpl:
    def getPersonByUsername(self,username):
        print "Get client " + username
        person = ttypes.Person()
        person.age = 22
        person.married = False
        person.username = username
        return person

    def savePerson(self,person):
        print person

try:
    personserviceHandler = PersonServiceImpl()
    processor = PersonService.Processor(personserviceHandler)

    serverSocket = TSocket.TServerSocket(port=8899)
    transportFactory = TTransport.TFramedTransportFactory()
    protocalfactory = TCompactProtocol.TCompactProtocolFactory()
    
    server = TServer.TSimpleServer(processor,serverSocket,transportFactory,protocalfactory)
    server.serve()
    
except Thrift.TException as ex: 
    print(ex)