package jie.runtime.rpc;

import jie.runtime.rpc.event.RpcExceptionEventArgs;
import jie.runtime.rpc.tcp.TcpRpcClient;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {

        TcpRpcClient client = new TcpRpcClient(new InetSocketAddress("217.0.0.1", 8080));
        IA a = client.resolver(IA.class);
        a.testMethod(10, 20.0F, "String", new RpcExceptionEventArgs(new NullPointerException()));

        System.out.println();
    }




    interface IA {
        int getA();

        void setA(int a);

        Integer getB();

        String testMethod(int a, Float b, String c, RpcExceptionEventArgs d);
    }
}
