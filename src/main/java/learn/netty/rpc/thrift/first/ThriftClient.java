package learn.netty.rpc.thrift.first;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;

/**
 * @Author: cmm
 * @Date: 19-7-24 下午4:59
 * @Version 1.0
 */
public class ThriftClient {
    public static void main(String[] args){
        TFramedTransport transport = new TFramedTransport(new TSocket("127.0.0.1", 8899), 600);
        TCompactProtocol protocol = new TCompactProtocol(transport);
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            transport.open();
            Person cmm = client.getPersonByName("CMM");
            client.savePerson(cmm);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            transport.close();
        }


    }
}
