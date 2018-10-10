package com.isel.client;

import java.io.IOException;

public interface IClient {
    void Start();
    void Initialize() throws IOException;
    void Stop();
}
