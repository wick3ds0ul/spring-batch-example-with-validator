package com.prabhakar.batch;

import com.prabhakar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.batch.api.chunk.ItemReader;
import java.io.Serializable;

@Component
public class CustomReader implements ItemReader  {
   @Autowired
   private UserRepository userRepository;


    @Override
    public void open(Serializable serializable) {

    }

    @Override
    public void close() {

    }

    @Override
    public Object readItem() {
        return userRepository.findAll();
    }

    @Override
    public Serializable checkpointInfo() {
        return null;
    }
}
