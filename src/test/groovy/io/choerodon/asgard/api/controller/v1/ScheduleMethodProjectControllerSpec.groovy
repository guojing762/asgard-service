package io.choerodon.asgard.api.controller.v1

import com.github.pagehelper.PageInfo
import io.choerodon.asgard.IntegrationTestConfiguration
import io.choerodon.asgard.api.vo.ScheduleMethodParams
import io.choerodon.asgard.app.service.ScheduleMethodService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
class ScheduleMethodProjectControllerSpec extends Specification {
    public static final String BASE_PATH = "/v1/schedules/projects/1/methods"
    @Autowired
    TestRestTemplate restTemplate

    @Autowired
    private ScheduleMethodProjectController scheduleMethodController

    private ScheduleMethodService mockScheduleMethodService = Mock(ScheduleMethodService)

    void setup() {
        scheduleMethodController.setScheduleMethodService(mockScheduleMethodService)
    }

    def "PagingQuery"() {
        given: 'queryParams准备'
        def code = "code"
        def service = "service"
        def method = "method"
        def description = "description"
        def params = "params"
        def queryParams = new HashMap<String, String>()
        queryParams.put("code", code)
        queryParams.put("service", service)
        queryParams.put("method", method)
        queryParams.put("description", description)
        queryParams.put("params", params)

        when: '对接口【分页查询执行方法列表】发送GET请求'
        def entity = restTemplate.getForEntity(BASE_PATH, PageInfo, queryParams, 1, 20)
        then: '状态码正确；方法参数调用成功'
        entity.statusCode.is2xxSuccessful()
        1 * mockScheduleMethodService.pageQuery(_, _, _, _, _, _, _, _)
    }

    def "GetMethodByService"() {
        given: "参数准备"
        def service = "service"

        when: '对接口【根据服务名获取方法】发送GET请求'
        def entity = restTemplate.getForEntity(BASE_PATH + "/service?service={service}", List, service)

        then: '状态码正确；方法参数调用成功'
        entity.statusCode.is2xxSuccessful()
        1 * mockScheduleMethodService.getMethodByService(_, _)
    }

    def "GetParams"() {
        given: "参数准备"
        def id = 1L

        when: '对接口【查看可执行程序详情】发送GET请求'
        def entity = restTemplate.getForEntity(BASE_PATH + "/{id}", ScheduleMethodParams, id)

        then: '状态码正确；方法参数调用成功'
        entity.statusCode.is2xxSuccessful()
        1 * mockScheduleMethodService.getParams(id, _)
    }
}
