package com.jaeckel.bitcoin;


import com.google.bitcoin.core.*;
import com.google.bitcoin.net.discovery.PeerDiscovery;
import com.google.bitcoin.net.discovery.PeerDiscoveryException;
import com.google.bitcoin.params.MainNetParams;
import com.google.bitcoin.store.*;
import com.google.bitcoin.utils.BriefLogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class FullClient {

    public static final Logger Log = LoggerFactory.getLogger(FullClient.class);

    private static final String DB_HOST = "localhost";
    private static final String DB_NAME = "full_mode_db2";
    private static final String DB_USER = "biafra";
    private static final String DB_PASSWORD = "";
    private static final String BITCOIND_HOSTNAME = "localhost";
    private static final int BITCOIND_PORT = 8333;

    private static NetworkParameters netParams = new MainNetParams();

    public static void main(String[] args) {
        BriefLogFormatter.init();
        connectBlockChain();
    }

    private static void connectBlockChain() {

        try {

            PostgresFullPrunedBlockStore blockStore = new PostgresFullPrunedBlockStore(netParams, 0, DB_HOST, DB_NAME, DB_USER, DB_PASSWORD);
            FullPrunedBlockChain blockChain = new FullPrunedBlockChain(netParams, blockStore);
            PeerDiscovery peerDiscovery = getLocalHostPeerDiscovery();

            //faster and insecure
            blockChain.setRunScripts(false);

            PeerGroup peerGroup = new PeerGroup(netParams, blockChain);
            peerGroup.addPeerDiscovery(peerDiscovery);

            peerGroup.start();
            Log.info("Starting up.");

        } catch (BlockStoreException e) {

            Log.error("Exception while opening block store", e);

        }

    }

    public static PeerDiscovery getLocalHostPeerDiscovery() {
        return new PeerDiscovery() {
            @Override
            public InetSocketAddress[] getPeers(long timeoutValue, TimeUnit timeoutUnit) throws PeerDiscoveryException {
                InetSocketAddress[] result = new InetSocketAddress[1];
                result[0] = new InetSocketAddress(BITCOIND_HOSTNAME, BITCOIND_PORT);
                return result;
            }

            @Override
            public void shutdown() {
                Log.info("shutDown");
            }
        };
    }

}
