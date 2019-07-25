namespace java learn.netty.rpc.thrift.first
namespace py learn.netty.rpc.thrift.py

typedef i16 short
typedef i32 int
typedef i16 long
typedef bool boolean
typedef string String

struct Person {
    1: optional String username,
    2: optional int age,
    3: optional boolean married
}

exception DataException {
    1: optional String message,
    2: optional String callStack,
    3: optional String date
}

service PersonService {
    Person getPersonByName (1:required String name) throws (1:DataException e),

    void savePerson(1:required Person person) throws (1:DataException e)
}