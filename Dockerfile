FROM sequenceiq/spark

RUN mkdir /root/govdata

ADD ./atividade1 /root/govdata
ADD ./atividade2 /root/govdata
ADD ./atividade3 /root/govdata



