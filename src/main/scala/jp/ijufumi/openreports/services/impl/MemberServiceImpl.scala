package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.{
  FunctionRepository,
  MemberRepository,
  RoleFunctionRepository,
  WorkspaceMemberRepository,
  WorkspaceRepository,
}
import jp.ijufumi.openreports.presentation.models.responses.{Member, Permissions}
import jp.ijufumi.openreports.services.MemberService

class MemberServiceImpl @Inject() (
    memberRepository: MemberRepository,
    workspaceMemberRepository: WorkspaceMemberRepository,
    roleFunctionRepository: RoleFunctionRepository,
    functionRepository: FunctionRepository,
    workspaceRepository: WorkspaceRepository,
) extends MemberService {
  override def update(memberId: String, name: String, password: String): Option[Member] = {
    val memberOpt = memberRepository.getById(memberId)
    if (memberOpt.isEmpty) {
      return None
    }
    val newMember = memberOpt.get.copy(name = name, password = password)
    memberRepository.update(newMember)
    memberRepository.getById(memberId).map(m => m.toResponse)
  }

  override def permissions(memberId: String, workspaceId: String): Option[Permissions] = {
    val workspaces = workspaceRepository.getsByMemberId(memberId)
    val workspaceMemberOpt = workspaceMemberRepository.getById(workspaceId, memberId)
    if (workspaceMemberOpt.isEmpty) {
      return None
    }
    val workspaceMember = workspaceMemberOpt.get
    val functionIds =
      roleFunctionRepository.getByRoleId(workspaceMember.roleId).map(m => m.functionId)
    val functions = functionRepository.getsByIds(functionIds)
    Some(Permissions(workspaces, functions.map((f => f.toResponse))))
  }
}
