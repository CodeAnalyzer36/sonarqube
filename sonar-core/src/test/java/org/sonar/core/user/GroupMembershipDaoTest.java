/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.core.user;

import org.junit.Before;
import org.junit.Test;
import org.sonar.core.persistence.AbstractDaoTestCase;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class GroupMembershipDaoTest extends AbstractDaoTestCase {

  private GroupMembershipDao dao;

  @Before
  public void setUp() {
    dao = new GroupMembershipDao(getMyBatis());
  }

  @Test
  public void select_all_groups_by_query() throws Exception {
    setupData("shared");

    GroupMembershipQuery query = GroupMembershipQuery.builder().userId(200L).build();
    List<GroupMembershipDto> result = dao.selectGroups(query);
    assertThat(result).hasSize(3);
  }

  @Test
  public void select_user_group() throws Exception {
    setupData("select_user_group");

    GroupMembershipQuery query = GroupMembershipQuery.builder().userId(201L).build();
    List<GroupMembershipDto> result = dao.selectGroups(query);
    assertThat(result).hasSize(1);

    GroupMembershipDto dto = result.get(0);
    assertThat(dto.getId()).isEqualTo(101L);
    assertThat(dto.getName()).isEqualTo("sonar-users");
    assertThat(dto.getUserId()).isEqualTo(201L);
  }

  @Test
  public void select_user_groups_by_query() throws Exception {
    setupData("shared");

    // 200 is member of 3 groups
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(200L).memberShip(GroupMembershipQuery.MEMBER_ONLY).build())).hasSize(3);
    // 201 is member of 1 group on 3
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(201L).memberShip(GroupMembershipQuery.MEMBER_ONLY).build())).hasSize(1);
    // 999 is member of 0 group
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(999L).memberShip(GroupMembershipQuery.MEMBER_ONLY).build())).isEmpty();
  }

  @Test
  public void select_groups_not_affected_to_a_user_by_query() throws Exception {
    setupData("shared");

    // 200 is member of 3 groups
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(200L).memberShip(GroupMembershipQuery.NOT_MEMBER).build())).isEmpty();
    // 201 is member of 1 group on 3
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(201L).memberShip(GroupMembershipQuery.NOT_MEMBER).build())).hasSize(2);
    // 999 is member of 0 group
    assertThat(dao.selectGroups(GroupMembershipQuery.builder().userId(999L).memberShip(GroupMembershipQuery.NOT_MEMBER).build())).hasSize(3);
  }

  @Test
  public void select_only_matching_group_name() throws Exception {
    setupData("shared");

    List<GroupMembershipDto> result = dao.selectGroups(GroupMembershipQuery.builder().userId(200L).searchText("user").build());
    assertThat(result).hasSize(1);

    assertThat(result.get(0).getName()).isEqualTo("sonar-users");

    result = dao.selectGroups(GroupMembershipQuery.builder().userId(200L).searchText("sonar").build());
    assertThat(result).hasSize(3);
  }

  @Test
  public void should_be_sorted_by_group_name() throws Exception {
    setupData("should_be_sorted_by_group_name");

    List<GroupMembershipDto> result = dao.selectGroups(GroupMembershipQuery.builder().userId(200L).memberShip(GroupMembershipQuery.ALL).build());
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getName()).isEqualTo("sonar-administrators");
    assertThat(result.get(1).getName()).isEqualTo("sonar-reviewers");
    assertThat(result.get(2).getName()).isEqualTo("sonar-users");
  }

}
