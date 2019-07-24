package learn.netty.rpc.thrift.first;

import org.apache.thrift.TException;

/**
 * @Author: cmm
 * @Date: 19-7-24 下午4:56
 * @Version 1.0
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByName(String name) throws DataException, TException {
        System.out.println("Got client Param:"+name);
        Person person = new Person();
        person.setAge(20);
        person.setMarried(false);
        person.setUsername(name);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println(person);
    }
}
