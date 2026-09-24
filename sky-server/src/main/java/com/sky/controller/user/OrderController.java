package com.sky.controller.user;


import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@Slf4j
@Api(tags="用户端订单订单相关接口")
@RequestMapping("user/order")

public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submint")
    public Result<OrderSubmitVO> ordersubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){

        log.info("用户端订单提交接口,ordersSubmitDTO:{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.orderSubmit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);

    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

}
