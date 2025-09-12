package impl.defaults;

import config.Tasks;
import impl.Manager;
import impl.ReplacerImpl;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Replacer implements ReplacerImpl {
    @Override
    public Request newRequest(Manager manager, Tasks config, long i) {
        return getRequest(config);
    }

    private Request getRequest(Tasks config){
        var builder=new Request.Builder()
                .url(config.url);

        if (config.body!=null){
            builder.method(config.method,
                    RequestBody.create(config.body.getBytes(StandardCharsets.UTF_8)));
        }

        for (Map.Entry<String,String> entry:config.headers.entrySet()){
            builder.addHeader(entry.getKey(),entry.getValue());
        }

        return builder.build();
    }
}
