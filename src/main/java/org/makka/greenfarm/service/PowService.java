package org.makka.greenfarm.service;

import org.makka.greenfarm.utils.Block;

public interface PowService {
    public Block mine();

    public boolean isValidHash(String hash);

}
