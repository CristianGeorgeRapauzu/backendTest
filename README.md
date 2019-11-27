# backendTest

The Problem

We’re asking you to build a specific monitoring application.

To add merchants to the GOLO app, we use the Paysafe Developer API to register and validate merchants. Here’s the link: https://developer.paysafe.com/en/partners/accounts/api.

In order to inform us of it’s current availability, the server API allows for external monitoring of its status(https://developer.paysafe.com/en/turnkey/accounts/api/#/reference/0/verify-that-the-service-is-accessible/verify-that-the-service-is-accessible ). Your task is to create an application that will monitor the status of the server and report on it when requested. You do not need to create an account, the monitoring endpoint does not need authentication.


The Requirements

The app should have REST endpoint(s) that allows to start or stop the monitoring of the Paysafe Developer API. In its payload, this endpoint(s) should accept at least these two values:
An interval field that tells your server what interval should be used between each request to the server(start).
A url field for the url value of the server to monitor. This value should be available when you start and stop the monitoring.

The app should have another REST endpoint which, when requested, gives an overview to the user of the time periods when the server was up and when it was down since the last time it was started.

The resulting monitoring data doesn’t have to be persistent.


The Solution

URLs:
https://developer.paysafe.com/en/turnkey/accounts/api/#/reference/0/verify-that-the-service-is-accessible/verify-that-the-service-is-accessible

https://api.test.paysafe.com/accountmanagement/monitor

Response:
{"status":"READY"}


POST start, stop, ping
{"url":"https://api.test.paysafe.com/accountmanagement/monitor","rate":3000}

GET status
?url=https://api.test.paysafe.com/accountmanagement/monitor

2019-11-27 16:46:50.313 TRACE 13809 --- [nio-8080-exec-1] i.g.b.c.BackendTestRestController        : GET status - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:46:50.314  INFO 13809 --- [nio-8080-exec-1] i.g.b.service.MonitoringService          : status - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor, no status accumulated yet
2019-11-27 16:47:43.933 TRACE 13809 --- [nio-8080-exec-2] i.g.b.c.BackendTestRestController        : POST start - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:47:43.934  INFO 13809 --- [nio-8080-exec-2] i.g.b.service.MonitoringService          : start - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:47:43.941  INFO 13809 --- [nio-8080-exec-2] i.g.b.service.MonitoringService          : start - scheduledFuture.isDone: false, scheduledFuture.isCancelled: false 
2019-11-27 16:47:43.943  INFO 13809 --- [nio-8080-exec-2] i.g.b.service.MonitoringService          : start - Under scrutiny: 1 | 1
2019-11-27 16:47:45.093  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:47.003  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:50.058  INFO 13809 --- [lTaskScheduler2] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:53.099  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:56.022  INFO 13809 --- [lTaskScheduler3] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:59.025  INFO 13809 --- [lTaskScheduler3] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:47:59.302 TRACE 13809 --- [nio-8080-exec-3] i.g.b.c.BackendTestRestController        : POST stop - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:47:59.302  INFO 13809 --- [nio-8080-exec-3] i.g.b.service.MonitoringService          : stop - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:47:59.302  INFO 13809 --- [nio-8080-exec-3] i.g.b.service.MonitoringService          : stop - scheduledFuture.isDone: false, scheduledFuture.isCancelled: false
2019-11-27 16:47:59.303  INFO 13809 --- [nio-8080-exec-3] i.g.b.service.MonitoringService          : stop - Under scrutiny: 0 | 0
2019-11-27 16:48:35.645 TRACE 13809 --- [nio-8080-exec-4] i.g.b.c.BackendTestRestController        : GET status - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:48:35.647  INFO 13809 --- [nio-8080-exec-4] i.g.b.service.MonitoringService          : status - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor of total: 1, status: [PeekWindow [startInstant=2019-11-27T21:47:43.942Z, stopInstant=2019-11-27T21:47:59.302Z, previousStatus=READY, flips=[]]]
2019-11-27 16:49:17.010 TRACE 13809 --- [nio-8080-exec-5] i.g.b.c.BackendTestRestController        : POST start - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:49:17.010  INFO 13809 --- [nio-8080-exec-5] i.g.b.service.MonitoringService          : start - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:49:17.011  INFO 13809 --- [nio-8080-exec-5] i.g.b.service.MonitoringService          : start - scheduledFuture.isDone: false, scheduledFuture.isCancelled: false 
2019-11-27 16:49:17.011  INFO 13809 --- [nio-8080-exec-5] i.g.b.service.MonitoringService          : start - Under scrutiny: 1 | 1
2019-11-27 16:49:17.139  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:49:22.076  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:49:27.221  INFO 13809 --- [lTaskScheduler1] i.g.b.service.MonitoringService          : start - https://api.test.paysafe.com/accountmanagement/monitor : AvailabilityDTO [status=READY]
2019-11-27 16:49:31.374 TRACE 13809 --- [nio-8080-exec-6] i.g.b.c.BackendTestRestController        : POST stop - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:49:31.375  INFO 13809 --- [nio-8080-exec-6] i.g.b.service.MonitoringService          : stop - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:49:31.375  INFO 13809 --- [nio-8080-exec-6] i.g.b.service.MonitoringService          : stop - scheduledFuture.isDone: false, scheduledFuture.isCancelled: false
2019-11-27 16:49:31.375  INFO 13809 --- [nio-8080-exec-6] i.g.b.service.MonitoringService          : stop - Under scrutiny: 0 | 0
2019-11-27 16:49:44.577 TRACE 13809 --- [nio-8080-exec-7] i.g.b.c.BackendTestRestController        : GET status - url: https://api.test.paysafe.com/accountmanagement/monitor
2019-11-27 16:49:44.578  INFO 13809 --- [nio-8080-exec-7] i.g.b.service.MonitoringService          : status - monitoring URL: https://api.test.paysafe.com/accountmanagement/monitor of total: 1, status: [PeekWindow [startInstant=2019-11-27T21:47:43.942Z, stopInstant=2019-11-27T21:47:59.302Z, previousStatus=READY, flips=[]], PeekWindow [startInstant=2019-11-27T21:49:17.011Z, stopInstant=2019-11-27T21:49:31.375Z, previousStatus=READY, flips=[]]]
