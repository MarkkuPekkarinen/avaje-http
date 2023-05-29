package example.github;

import com.google.gson.Gson;
import io.avaje.http.client.BodyAdapter;
import io.avaje.http.client.HttpClient;
import io.avaje.http.client.JacksonBodyAdapter;
import io.avaje.http.client.gson.GsonBodyAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GithubTest {

  @Test
  void test_create() {
    final HttpClient client = HttpClient.builder().baseUrl("https://api.github.com").build();

    final Simple simple = client.create(Simple.class);
    assertThat(simple).isNotNull();
  }

  @Disabled
  @Test
  void test_with_jackson() {
    assertListRepos(jacksonBodyAdapter());
  }

  @Disabled
  @Test
  void test_with_gson() {
    assertListRepos(gsonBodyAdapter());
  }

  private void assertListRepos(BodyAdapter bodyAdapter) {
    final HttpClient client = HttpClient.builder()
      .baseUrl("https://api.github.com")
      .bodyAdapter(bodyAdapter)
//      .requestLogging(false)
//      .requestListener(new RequestLogger())
      .build();

    final Simple simple = client.create(Simple.class);

    final List<Repo> repos = simple.listRepos("rbygrave", "junk");
    assertThat(repos).isNotEmpty();
  }

  private BodyAdapter jacksonBodyAdapter() {
    return new JacksonBodyAdapter();
  }

  private BodyAdapter gsonBodyAdapter() {
    return new GsonBodyAdapter(new Gson());
  }
}
