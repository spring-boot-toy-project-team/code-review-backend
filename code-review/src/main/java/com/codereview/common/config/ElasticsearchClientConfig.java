package com.codereview.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchClientConfig {

  @Value("${elasticsearch.host}")
  private String host;

  @Value("${elasticsearch.port}")
  private int port;

  @Bean
  public ElasticsearchTransport elasticsearchTransport() {
    RestClient restClient = RestClient.builder(
      new HttpHost(host, port)).build();

    return new RestClientTransport(
      restClient, new JacksonJsonpMapper());
  }

  @Bean
  public ElasticsearchClient client() {
    return new ElasticsearchClient(elasticsearchTransport());
  }
}