package net.komov;

import com.owlike.genson.Genson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(3);

        try (JedisPool pool = new JedisPool(poolConfig, "localhost")) {
            handler.addServletWithMapping(new ServletHolder(new RedisServlet(pool)), "/");
            server.start();
            server.join();
        }
    }

    public static class RedisServlet extends HttpServlet {
        private JedisPool pool;

        public RedisServlet(JedisPool p) {
            this.pool = p;
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try (Jedis jedis = this.pool.getResource()) {
                Genson genson = new Genson();
                Data d = genson.deserialize(req.getReader(), Data.class);
                d.value = jedis.getSet("k", d.value);
                resp.getWriter().println(genson.serialize(d));
            }
        }
    }

    public static class Data {
        public String value;
    }
}
