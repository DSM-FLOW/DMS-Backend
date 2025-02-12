package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service

@Service
class VoteService(
    checkVoteService: CheckVoteService,
    getVoteService: GetVoteService,
    commandVoteService: CommandVoteService
) : CheckVoteService by checkVoteService,
    GetVoteService by getVoteService,
    CommandVoteService by commandVoteService
