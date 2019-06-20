package hello;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class GreetingController {

    private static final Logger LOG = LoggerFactory.getLogger(GreetingController.class);

    private static final String template = "Hello, %s! Are you feeling %s?";

    private final Counter counter;

    @Value(value = "${COLOUR:orange}")
    private String colour;

    public GreetingController() {
       counter = new Counter("greeting_counter");
    }

    @RequestMapping(value = "/v1/greeting" ,  method = RequestMethod.GET)
    @ApiOperation(value = "Generate a greeting for a given name")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        counter.increment();

        LOG.info("Sending a greeting out to '{}', hope they're {}...", name, colour);

        return new Greeting((int)counter.count(),
                            String.format(template, name, colour));
    }

    @GetMapping("/")
    public String index() {
        return "Success!";
    }

    @RequestMapping(value = "/v1/hostinfo",  method = RequestMethod.GET)
    @ApiOperation(value = "List host name running the sample application.")
    public HostInfo hostinfo() throws IOException {
      return new HostInfo();
    }
    
    @RequestMapping(value = "/v1/envinfo" ,  method = RequestMethod.GET)
    @ApiOperation(value = "Display environment variables.")
    public EnvInfo envinfo(@RequestParam(value="filter", defaultValue="*") String filter) throws IOException {
      return new EnvInfo(filter);
    }   
}
