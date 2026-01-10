package com.ree.sireleves.service.batch;

import jakarta.annotation.PostConstruct;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OdooXmlRpcClient {

    @Value("${odoo.api.url}")
    private String url;

    @Value("${odoo.api.database}")
    private String db;

    @Value("${odoo.api.username}")
    private String username;

    @Value("${odoo.api.password}")
    private String password;

    private Integer uid;

    private XmlRpcClient objectClient;

    @PostConstruct
    public void init() throws Exception {

        // --- Login ---
        XmlRpcClientConfigImpl commonConfig = new XmlRpcClientConfigImpl();
        commonConfig.setServerURL(new URL(url + "/xmlrpc/2/common"));

        XmlRpcClient commonClient = new XmlRpcClient();
        commonClient.setConfig(commonConfig);

        uid = (Integer) commonClient.execute(
                "authenticate",
                List.of(db, username, password, Map.of())
        );

        if (uid == null) {
            throw new IllegalStateException("❌ Authentification Odoo échouée");
        }

        // --- Object client ---
        XmlRpcClientConfigImpl objectConfig = new XmlRpcClientConfigImpl();
        objectConfig.setServerURL(new URL(url + "/xmlrpc/2/object"));

        objectClient = new XmlRpcClient();
        objectClient.setConfig(objectConfig);
    }

    @SuppressWarnings("unchecked")
    public Object[] fetchClients() throws Exception {

        return (Object[]) objectClient.execute(
                "execute_kw",
                List.of(
                        db,
                        uid,
                        password,
                        "res.partner",
                        "search_read",
                        List.of(
                                List.of(List.of("customer_rank", ">", 0))
                        ),
                        Map.of(
                                "fields", List.of(
                                        "id", "name", "phone", "email",
                                        "street", "city"
                                )
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    public Object[] fetchAddresses() throws Exception {

        return (Object[]) objectClient.execute(
                "execute_kw",
                List.of(
                        db,
                        uid,
                        password,
                        "res.partner",
                        "search_read",
                        List.of(
                                List.of(
                                        List.of("parent_id", "!=", false)
                                )
                        ),
                        Map.of(
                                "fields", List.of(
                                        "id",
                                        "parent_id",
                                        "street",
                                        "street2",
                                        "zip",
                                        "city",
                                        "type"
                                )
                        )
                )
        );
    }
    @SuppressWarnings("unchecked")
    public Object[] fetchAgents() throws Exception {

        return (Object[]) objectClient.execute(
                "execute_kw",
                List.of(
                        db,
                        uid,
                        password,
                        "hr.employee",
                        "search_read",
                        List.of(List.of()),
                        Map.of(
                                "fields", List.of(
                                        "id",
                                        "name",
                                        "work_phone",
                                        "mobile_phone",
                                        "department_id"
                                )
                        )
                )
        );
    }

}

