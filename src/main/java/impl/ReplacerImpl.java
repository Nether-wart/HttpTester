package impl;

import config.Tasks;
import okhttp3.Request;

public interface ReplacerImpl {
    Request newRequest(Manager manager,Tasks config,long i);
}
