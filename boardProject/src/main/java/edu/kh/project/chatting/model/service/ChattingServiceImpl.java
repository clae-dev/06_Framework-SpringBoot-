package edu.kh.project.chatting.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ChattingServiceImpl implements ChattingService {

}
